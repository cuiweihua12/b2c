package com.cwh.service.impl;

import com.cwh.client.ConnectionBrandClient;
import com.cwh.client.ConnectionCategoryClient;
import com.cwh.client.ConnectionGoodsClient;
import com.cwh.client.ConnectionSpecClient;
import com.cwh.entity.*;
import com.cwh.service.ItemService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-17 20:19
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ConnectionCategoryClient categoryClient;

    @Autowired
    private ConnectionBrandClient brandClient;

    @Autowired
    private ConnectionSpecClient specClient;

    @Autowired
    private ConnectionGoodsClient goodsClient;
    @Autowired
    private TemplateEngine templateEngine;

    private Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Override
    public Map<String, Object> querySpuById(Long id) {
        //spu
        Spu spu = goodsClient.querySpuById(id).getBody();
        //分类
        List<Category> categoryList = categoryClient.queryCategory(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())).getBody();
        //规格参数
        List<SpecGroup> groupList = specClient.searchSpecGroupByCategoruId(spu.getCid3()).getBody();
        groupList.forEach(group->{
            group.setParams(specClient.searchSpecParamsBySpecGroupId(group.getId(),null,null).getBody());
        });
        //sku
        List<Sku> skuList = goodsClient.querySkusBySpuId(id).getBody();
        //spuDatile
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(id).getBody();
        //brand
        Brand brand = brandClient.queryByBrandId(spu.getBrandId()).getBody();
        //特有规格
        List<SpecParam> specParams = specClient.searchSpecParamsBySpecGroupId(null, spu.getCid3(), false).getBody();
        HashMap<Long, String> specParamsMap = new HashMap<>();
        specParams.forEach(specParam -> {
            specParamsMap.put(specParam.getId(),specParam.getName());
        });
        Map<String, Object> map = new HashMap<>();
        map.put("sku",skuList);
        map.put("spu",spu);
        map.put("category",categoryList);
        map.put("spuDetail",spuDetail);
        map.put("brand",brand);
        map.put("specParams",specParamsMap);
        map.put("gorup",groupList);
        return map;
    }

    // 自己手动创建一个存在的文件路径
    public static final String destPath = "D:/temp/static/item";



    /**
    *@Description: 商品详情页静态化，并监听rabbitmq消息队列，当item对商品修改后重新静态化页面
    *@Param:
    *@return:
    *@Author: cuiweihua
    *@date: 2020/8/20
    */
    @SneakyThrows
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "html.static",durable = "true"),
                    exchange = @Exchange(
                            value = "b2c",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.DIRECT
                    ),
                    key = {"item.save","item.update","item.delete","item.off"}
            )
    })
    public  void createHTML(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) {
        String id = new String(message.getBody());
        logger.info("静态化页面服务启动，id===========>"+id);
        //上下文
        Context context = new Context();
        channel.basicQos(1);
        context.setVariables(querySpuById(Long.parseLong(id)));
        //输出流
        File file = new File(destPath,id+".html");
        //查看文件是否存在，如存在则删除重新创建
        if (file.exists()){
            file.delete();
        }
        PrintWriter printWriter =null;
        try {
            printWriter = new PrintWriter(file, "UTF-8");
            templateEngine.process("item",context, printWriter);
            /**
             * 无异常确认消息
             */
            channel.basicAck(tag,false);
        } catch (Exception e) {
            e.printStackTrace();
            /**
             * 有异常就绝收消息
             * requeue:true为将消息重返当前消息队列,还可以重新发送给消费者;
             *          false:将消息丢弃
             */
            channel.basicNack(tag,false,true);
        }finally {
            printWriter.close();
        }
    }

    public  void createHTMLJuint(Long spuId) {
        logger.info("静态化页面服务启动，id===========>"+ spuId);
        //上下文
        Context context = new Context();
        context.setVariables(querySpuById(spuId));
        //输出流
        File file = new File(destPath,spuId+".html");
        //查看文件是否存在，如存在则删除重新创建
        if (file.exists()){
            file.delete();
        }
        PrintWriter printWriter =null;
        try {
            printWriter = new PrintWriter(file, "UTF-8");
            templateEngine.process("item",context, printWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            printWriter.close();
        }
    }
}

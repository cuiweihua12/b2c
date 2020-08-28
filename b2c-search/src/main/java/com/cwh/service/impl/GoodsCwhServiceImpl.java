package com.cwh.service.impl;

import com.cwh.bo.BrandBo;
import com.cwh.bo.CategoryBo;
import com.cwh.bo.SpecParamBo;
import com.cwh.client.ConnectionBrandClient;
import com.cwh.client.ConnectionCategoryClient;
import com.cwh.client.ConnectionGoodsClient;
import com.cwh.client.ConnectionSpecClient;
import com.cwh.common.utils.JsonUtils;
import com.cwh.dao.*;
import com.cwh.entity.*;
import com.cwh.pojo.GoodsCWH;
import com.cwh.pojo.Pages;
import com.cwh.pojo.SearchResult;
import com.cwh.service.GoodsCWHService;
import com.cwh.utils.HightLightUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-10 15:32
 */
@Service
public class GoodsCwhServiceImpl implements GoodsCWHService {

    @Autowired
    private GoodsCWHRepository goodsCWHRepository;

    @Autowired
    private BrandsRepostitory brandsRepostitory;

    @Autowired
    private CategoryRespostory categoryRespostory;

    @Autowired
    private ConnectionSpecClient specClient;

    @Autowired
    private ConnectionGoodsClient goodsClient;

    @Autowired
    private ConnectionBrandClient brandClient;

    @Autowired
    private ConnectionCategoryClient categoryClient;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ParamsRespository paramsRespository;

    @Autowired
    private GoodsBoRepository goodsBoRepository;

    private Logger logger = LoggerFactory.getLogger(GoodsCwhServiceImpl.class);

    /**
    *@Description: 通过前台搜索框输入查询 并分页
    *@Param: [page]
    *@return: com.cwh.common.utils.PageResult<com.cwh.pojo.GoodsCWH>
    *@Author: cuiweihua
    *@date: 2020/8/11
    */
    @Override
    public SearchResult<GoodsCWH> searchGoods(Pages page) {
        if(page.getPage() == null || page.getPage() < 1){
            page.setPage(0);
        }else {
            page.setPage(page.getPage()-1);
        }
        if (page.getSize() == null || page.getSize() < 1 ){
            page.setSize(20);
        }
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //条件
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        queryBuilder .should(QueryBuilders.matchQuery("title",page.getKey()))
                .should(QueryBuilders.matchQuery("brand",page.getKey()))
                .should(QueryBuilders.matchQuery("category",page.getKey()));
        //选中条件过滤
        if (!StringUtils.isEmpty(page.getFilterMap())){
            Map<String, String> filterMap = JsonUtils.parseMap(page.getFilterMap(), String.class, String.class);
            Iterator<String> iterator = filterMap.keySet().iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                switch (key){
                    case "cid3":
                    case "brandId":
                        queryBuilder.must(QueryBuilders.matchQuery(key,filterMap.get(key)));break;
                    default:queryBuilder.must(QueryBuilders.matchQuery("specs."+key+".keyword",filterMap.get(key)));
                }
            }
        }
        builder.withQuery(queryBuilder);
        if (!StringUtils.isEmpty(page.getSortBy())){
            //排序
            builder.withSort(page.getSort()?SortBuilders.fieldSort(page.getSortBy()):SortBuilders.fieldSort(page.getSortBy()).order(SortOrder.DESC));
        }
        //分页
        builder.withPageable(PageRequest.of(page.getPage(),page.getSize()));
        //高亮
        builder.withHighlightFields((
                new HighlightBuilder.Field("title")
                        .preTags("<span style='color:red'>")
                        .postTags("</span>")));
        //结果过滤 只显示那些字段
        builder.withSourceFilter(new SourceFilter() {
            @Override  //显示的字段
            public String[] getIncludes() {
                return new String[]{"id","skus","subTitle","title"};
            }
            @Override //忽略的字段
            public String[] getExcludes() {
                return new String[0];
            }
        });


        //聚合查询 品牌和商品分类
        builder.addAggregation(AggregationBuilders.terms("cate_group").field("cid3"));
        builder.addAggregation(AggregationBuilders.terms("brand_group").field("brandId"));

        //查询
        Page<GoodsCWH> search = goodsCWHRepository.search(builder.build());
        if (search.getContent().isEmpty()) {
            return null;
        }
        //高亮查询转换
        Map<Long, String> all = HightLightUtil.getHignLigntMap(elasticsearchTemplate, builder.build(), GoodsCWH.class, "title");
        search.forEach(se->{
            se.setTitle(all.get(se.getId()));
        });

        //获取分类聚合查询结构
        AggregatedPage<GoodsCWH> aggregatedPage = (AggregatedPage<GoodsCWH>) search;
        Aggregations groupAggregations = aggregatedPage.getAggregations();
        //获取分类聚合结果
        LongTerms cate_group = groupAggregations.get("cate_group");
        //获取分类id 查询分类信息
        List<CategoryBo> categoryList = searchCategoryEs(cate_group.getBuckets()
                .stream()
                .map(LongTerms.Bucket::getKeyAsString)
                .toArray(String[]::new)
        );
        //获取品牌分类聚合查询结果
        LongTerms brandGroup = groupAggregations.get("brand_group");
        //后期修改，不走数据库，查询es
        List<BrandBo> brandList = searchBrandEs(brandGroup.getBuckets()
                .stream()
                .map(LongTerms.Bucket::getKeyAsString)
                .toArray(String[]::new)
        );

        //获取分类商品最多的分类id
            //获取所有分类条数
        long maxCateId = 0;
        long maxCount = cate_group.getBuckets().stream().mapToLong(InternalTerms.Bucket::getDocCount).max().getAsLong();
        for (LongTerms.Bucket bucket : cate_group.getBuckets()) {
            if (bucket.getDocCount() == maxCount){
                maxCateId = (long) bucket.getKey();
                break;
            }
        }
        //根据商品分类获取商品规格参数
        List<SpecParamBo> paramList = searchParamsEs(maxCateId);
        List<Map<String, Object>> specMapList = null;
        if (!paramList.isEmpty()){
            specMapList = searchSpec(builder, paramList,page);
        }
        //打印query的 的dsl语句
        logger.info(builder.build().getQuery().toString());
        return new SearchResult<GoodsCWH>(search.getTotalElements(),(long)search.getTotalPages(),search.getContent(),categoryList,brandList,specMapList);
    }

    /**
     * 根据品牌id查询符合条件的品牌
     */
    public List<BrandBo> searchBrandEs(String[] ids){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("id",ids)));
        builder.withPageable(PageRequest.of(0,10000));
        Page<BrandBo> search = brandsRepostitory.search(builder.build());
        return search.getContent();
    }

    /**
     * 根据分类id查询符合条件的分类
     */
    public List<CategoryBo> searchCategoryEs(String[] cids){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("id",cids)));
        builder.withPageable(PageRequest.of(0,10000));
        Page<CategoryBo> search = categoryRespostory.search(builder.build());
        return search.getContent();
    }

    /**
     * 根据字段查询es库
     */
    public List<SpecParamBo> searchParamsEs(Long cid){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(QueryBuilders.matchQuery("cid",cid));
        builder.withPageable(PageRequest.of(0,10000));
        Page<SpecParamBo> search = paramsRespository.search(builder.build());
        return search.getContent();
    }

    /**
    *@Description: 根据参数信息聚合查询
    *@Param: NativeSearchQueryBuilder List<SpecParam>
    *@return:  List<Map<String,Object>>
    *@Author: cuiweihua
    *@date: 2020/8/12
    */
    public List<Map<String,Object>> searchSpec(NativeSearchQueryBuilder nativeSearchQueryBuilder, List<SpecParamBo> param, Pages page){
        List<Map<String,Object>> list = new ArrayList<>();
        //添加聚合查询条件
        for (SpecParamBo specParam : param) {
            if(specParam.getSearching()){
                nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs."+specParam.getName()+".keyword"));
            }
        }
        //过滤选中条件
//        searchFilter(nativeSearchQueryBuilder, page);
        //查询
        AggregatedPage<GoodsCWH> search = (AggregatedPage<GoodsCWH>) goodsCWHRepository.search(nativeSearchQueryBuilder.build());
        //获取聚合查询结果放入list
        Map<String, Object> specMap = null;
        for (SpecParamBo specParam : param) {
            if (specParam.getSearching()){
                specMap = new HashMap<>();
                specMap.put("key",specParam.getName());
                StringTerms searchAggregation = (StringTerms) search.getAggregation(specParam.getName());
                specMap.put("values",searchAggregation.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString).collect(Collectors.toList()));
                list.add(specMap);
            }
        }
        return list;
    }


    /**
    *@Description: 数据保存到es
    *@Param: [goods]
    *@return: void
    *@Author: cuiweihua
    *@date: 2020/8/12
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void  saveGoods(GoodsCWH goods) {
        goodsCWHRepository.save(goods);
    }


    /**
    *@Description: 保存品牌信息至es
    *@Param: [brand]
    *@return: void
    *@Author: cuiweihua
    *@date: 2020/8/21
    */
    @SneakyThrows
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "search.brand.save",durable = "true"),
                    exchange = @Exchange(value = "b2c",type = ExchangeTypes.DIRECT,ignoreDeclarationExceptions = "true"),
                    key = {"item.brand.save"}
            )
    })
    public void saveBrand(Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) Long tag,Message message){
        try {
            String brandJson = new String(message.getBody());
            Brand brand = JsonUtils.parse(brandJson, Brand.class);
            logger.info("es保存品牌："+brand.getName());
            channel.basicQos(1);
            BrandBo brandBo = new BrandBo();
            BeanUtils.copyProperties(brand, brandBo);
            brandsRepostitory.save(brandBo);
            channel.basicAck(tag,false);
        } catch (BeansException e) {
            e.printStackTrace();
            channel.basicNack(tag,false,true);
        }

    }

    /**
    *@Description: 移除es库中品牌数据
    *@Param:
    *@return:
    *@Author: cuiweihua
    *@date: 2020/8/21
    */
    @SneakyThrows
    @RabbitListener(bindings = {
        @QueueBinding(
                value = @Queue(value = "search.brand.remove",durable = "true"),
                exchange = @Exchange(value = "b2c",ignoreDeclarationExceptions = "true",
                type = ExchangeTypes.DIRECT
                ),
                key = {"item.brand.remove"}
            )
        })
    public void removeBrand(Long id,Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) Long tag,Message message){
        String strId = new String(message.getBody());
        logger.info("删除品牌："+strId);
        try {
            channel.basicQos(1);
            brandsRepostitory.deleteById(Long.parseLong(strId));
            channel.basicAck(tag,false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(tag,false,true);
        }
    }

    /**
    *@Description: 将规格参数保存至es
    *@Param:
    *@return:
    *@Author: cuiweihua
    *@date: 2020/8/21
    */
    @SneakyThrows
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "search.params.save",durable = "true"),
                    exchange = @Exchange(
                            type = ExchangeTypes.DIRECT,
                            ignoreDeclarationExceptions = "true",
                            value = "b2c"
                    ),
                    key = {"item.params.save"}
            )
    })
    public void saveSpecParams(Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag,Message message){
        try {
            String paramsJson = new String(message.getBody());
            SpecParam specParam = JsonUtils.parse(paramsJson, SpecParam.class);
            logger.info("保存参数至es ===========>"+specParam);
            channel.basicQos(1);
            SpecParamBo bo = new SpecParamBo();
            BeanUtils.copyProperties(specParam, bo);
            paramsRespository.save(bo);
            channel.basicAck(tag,false);
        } catch (BeansException e) {
            e.printStackTrace();
            channel.basicNack(tag,false,true);
        }

    }

    /**
    *@Description: 保存分类至es
    *@Param:
    *@return:
    *@Author: cuiweihua
    *@date: 2020/8/21
    */
    @SneakyThrows
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "search.category.save",durable = "true"),
                    exchange = @Exchange(value = "b2c",ignoreDeclarationExceptions = "true"),
                    key = {"category.save"}
            )
    })
    public void saveCategory(Message message,Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) Long tag){
        try {
            String categoryJson = new String(message.getBody());
            Category category = JsonUtils.parse(categoryJson, Category.class);
            CategoryBo categoryBo = new CategoryBo();
            BeanUtils.copyProperties(category, categoryBo);
            categoryRespostory.save(categoryBo);
            channel.basicAck(tag,false);
        } catch (BeansException e) {
            e.printStackTrace();
            channel.basicNack(tag,false,true);
        }
    }

    /**
    *@Description: 删除es中商品分类
    *@Param:
    *@return:
    *@Author: cuiweihua
    *@date: 2020/8/21
    */
    @SneakyThrows
    @RabbitListener(bindings = {
        @QueueBinding(
                value = @Queue(value = "search.category.remove",durable = "true"),
                exchange = @Exchange(value = "b2c",ignoreDeclarationExceptions = "true"),
                key = {"category.remove"}
        )
    })
    public void removeCategoryEs(Message message,Channel channel){
        try {
            categoryRespostory.deleteById(Long.parseLong(new String(message.getBody())));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
    }

    /**
     *@Description: 删除es库中规格参数
     *@Param:
     *@return:
     *@Author: cuiweihua
     *@date: 2020/8/21
     */
    @SneakyThrows
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "search.params.remove",durable = "true"),
                    exchange = @Exchange(
                            type = ExchangeTypes.DIRECT,
                            ignoreDeclarationExceptions = "true",
                            value = "b2c"
                    ),
                    key = {"item.params.remove"}
            )
    })
    public void removeSpecParams(Long id,Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) Long tag,Message message){
        try {
            logger.info("删除参数 ===========>"+id);
            channel.basicQos(1);
            paramsRespository.deleteById(Long.parseLong(new String(message.getBody())));
            channel.basicAck(tag,false);
        } catch (IOException e) {
            e.printStackTrace();
            channel.basicNack(tag,false,true);
        }
    }



    /**
    *@Description: 删除es库中数据，根据spuid
    *@Param:
    *@return:
    *@Author: cuiweihua
    *@date: 2020/8/20
    */
    @SneakyThrows
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "search.goods.remove",durable = "true"),
                    exchange = @Exchange(
                            value = "b2c",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.DIRECT

                    ),
                    key = {"item.off","item.delete"}

            )
    })
    public void deleteGoodsEs(Message message,Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) Long tag){
        try {
            String strId = new String(message.getBody());
            logger.info("删除es库中spu为："+strId+"的数据！");
            channel.basicQos(1);
            goodsCWHRepository.deleteById(Long.parseLong(new String(strId)));
            channel.basicAck(tag,false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(tag,false,true);
        }
    }


    /**
    *@Description: 将数据保存至es库  ,监听rabbitmq消息队列，当item服务新增商品后自动将数据保存至es
    *@Param:
    *@return:
    *@Author: cuiweihua
    *@date: 2020/8/20
    */
    @SneakyThrows
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "search.goods.save",durable = "true"),
                    exchange = @Exchange(
                            value = "b2c",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.DIRECT
                    ),
                    key = {"item.save","item.update"}
            )
    })
    public void getGoods(Long spuId, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag, Message message){
        try {
            String strId = new String(message.getBody());
            logger.info("将id为"+strId+"的spu保存至es");
            GoodsCWH goods = null;
            //商品集合
            List<GoodsCWH> goodsList = new ArrayList<>();
            //价格集合
            List<Long> priceList = null;

            //sku集合
            List<Map> skuList = null;
            //储存需要的skus
            HashMap<String, Object> hashMap = null;
            StringBuffer buffer = null;
            channel.basicQos(1);
            List<Spu> items = Arrays.asList(goodsClient.querySpuById(Long.parseLong(strId)).getBody());
        /*List<Spu> items = goodsClient
                .list(null, true, 0, 10000, "", 2)
                .getBody()
                .getItems();*/
            for (Spu item : items) {

                buffer = new StringBuffer();

                //品牌
                Brand brandEntity = brandClient.queryByBrandId(item.getBrandId()).getBody();
                //品牌名称
                String brand = brandEntity.getName();
                buffer.append(brandClient.queryByBrandId(item.getBrandId()).getBody().getName());
                //标题
                buffer.append(item.getTitle());
                //分类
                String category = categoryClient.queryCategory(
                        Arrays.asList(item.getCid1(), item.getCid2(), item.getCid3())
                ).getBody()
                        .stream()
                        .map(Category::getName)
                        .collect(Collectors.joining());
                buffer.append(
                        categoryClient.queryCategory(
                                Arrays.asList(item.getCid1(),item.getCid2(),item.getCid3())
                        ).getBody()
                                .stream()
                                .map(Category::getName)
                                .collect(Collectors.joining())
                );

                //skus 将获取到的数据进行抽取，只保存自己需要的
                List<Sku> skusList = goodsClient.querySkusBySpuId(item.getId()).getBody();

                skuList = new ArrayList<>();
                for (Sku skus : skusList) {
                    priceList = new ArrayList<>();
                    hashMap = new HashMap<>();
                    hashMap.put("id",skus.getId());
                    hashMap.put("price",skus.getPrice());
                    hashMap.put("image", StringUtils.isEmpty(skus.getImages())?"":skus.getImages());
                    hashMap.put("title",skus.getTitle());
                    skuList.add(hashMap);
                    //价格集合
                    priceList.add(skus.getPrice());
                }
                //规格参数值
                SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(item.getId()).getBody();
                //通用属性
                Map<Long, String> genericeMap = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
                //特殊属性
                //Map<Long, List> specialMap = JsonUtils.parseMap(spuDetail.getSpecialSpec(), Long.class, List.class);
                Map<Long, List<String>> specialMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>(){});

                //商品规格
                Map<String, Object>  map = new HashMap<>();
                //  规格参数
                specClient.searchSpecParamsBySpecGroupId(null, item.getCid3(),null)
                        .getBody()
                        //将参数与参数值做数据整合
                        .forEach(specParam -> {
                            if (specParam.getSearching()){
                                if (specParam.getGeneric()){
                                    if (specParam.getNumeric()){
                                        map.put(specParam.getName(),chooseSegment(genericeMap.get(specParam.getId()),specParam));
                                    }else {
                                        map.put(specParam.getName(),genericeMap.get(specParam.getId()));
                                    }
                                }else {
                                    map.put(specParam.getName(),specialMap.get(specParam.getId()));
                                }
                            }
                        });
                goods = new GoodsCWH(
                        item.getId(),
                        item.getTitle(),
                        brand,
                        category,
                        item.getSubTitle(),
                        item.getBrandId(),
                        item.getCid1(),
                        item.getCid2(),
                        item.getCid3(),
                        item.getCreateTime(),
                        priceList,
                        JsonUtils.serialize(skuList),
                        map
                );
                goodsList.add(goods);
            }
            goodsCWHRepository.saveAll(goodsList);
            channel.basicAck(tag,false);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(tag,false,true);
        }
    }
    /**
     * 将规格参数分段
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

}

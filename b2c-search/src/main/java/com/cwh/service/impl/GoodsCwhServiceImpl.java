package com.cwh.service.impl;

import com.cwh.client.ConnectionBrandClient;
import com.cwh.client.ConnectionCategoryClient;
import com.cwh.client.ConnectionGoodsClient;
import com.cwh.client.ConnectionSpecClient;
import com.cwh.common.utils.JsonUtils;
import com.cwh.dao.GoodsCWHRepository;
import com.cwh.entity.*;
import com.cwh.pojo.GoodsCWH;
import com.cwh.pojo.Pages;
import com.cwh.pojo.SearchResult;
import com.cwh.service.GoodsCWHService;
import com.cwh.utils.HightLightUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private ConnectionGoodsClient goodsClient;

    @Autowired
    private ConnectionBrandClient brandClient;

    @Autowired
    private ConnectionCategoryClient categoryClient;

    @Autowired
    private ConnectionSpecClient specClient;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

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
        builder.withQuery(QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("title",page.getKey()))
                .should(QueryBuilders.matchQuery("brand",page.getKey()))
                .should(QueryBuilders.matchQuery("category",page.getKey()))
        );
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
        //结果过滤
        builder.withSourceFilter(new SourceFilter() {
            @Override
            public String[] getIncludes() {
                return new String[]{"id","skus","subTitle","title"};
            }
            @Override
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
        List<Category> categoryList = categoryClient.queryCategory(cate_group.getBuckets()
                .stream()
                .map(LongTerms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList()))
                .getBody();
        //获取品牌分类聚合查询结果
        LongTerms brandGroup = groupAggregations.get("brand_group");
        List<Brand> brandList = brandClient.queryByIdList(brandGroup.getBuckets()
                .stream()
                .map(LongTerms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList()))
                .getBody();

        //获取分类商品最多的分类id
            //获取所有分类条数
        long maxCateId = 0;
        long maxCount = cate_group.getBuckets().stream().mapToLong(e -> e.getDocCount()).max().getAsLong();
        for (LongTerms.Bucket bucket : cate_group.getBuckets()) {
            if (bucket.getDocCount() == maxCount){
                maxCateId = (long) bucket.getKey();
                break;
            }
        }
        //根据商品分类获取商品规格参数
        System.out.println(maxCateId);
        List<SpecParam> paramList = specClient.searchSpecParamsBySpecGroupId(null,maxCateId).getBody();
        List<Map<String, Object>> specMapList = null;
        if (!paramList.isEmpty()){
            specMapList = searchSpec(builder, paramList);
        }
        //打印query的 的dsl语句
        logger.info(builder.build().getQuery().toString());
        return new SearchResult<GoodsCWH>(search.getTotalElements(),(long)search.getTotalPages(),search.getContent(),categoryList,brandList,specMapList);
    }


    /**
    *@Description: 根据参数信息聚合查询
    *@Param: NativeSearchQueryBuilder List<SpecParam>
    *@return:  List<Map<String,Object>>
    *@Author: cuiweihua
    *@date: 2020/8/12
    */
    public List<Map<String,Object>> searchSpec(NativeSearchQueryBuilder nativeSearchQueryBuilder , List<SpecParam> param){
        List<Map<String,Object>> list = new ArrayList<>();
        //添加聚合查询条件
        for (SpecParam specParam : param) {
            if(specParam.getSearching()){
                nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs."+specParam.getName()+".keyword"));
            }
        }
        //查询
        AggregatedPage<GoodsCWH> search = (AggregatedPage<GoodsCWH>) goodsCWHRepository.search(nativeSearchQueryBuilder.build());
        //获取聚合查询结果放入list
        Map<String, Object> specMap = null;
        for (SpecParam specParam : param) {
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
    @Transactional
    public void  saveGoods(GoodsCWH goods) {
        goodsCWHRepository.save(goods);
    }

    /**
     * 将数据库数据保存值es
     */
    public void getGoods(){
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
        List<Spu> items = goodsClient
                .list(null, true, 0, 10000, "", 2)
                .getBody()
                .getItems();
        for (Spu item : items) {

            buffer = new StringBuffer();

            //品牌
            String brand = brandClient.queryByBrandId(item.getBrandId()).getBody().getName();
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
            specClient.searchSpecParamsBySpecGroupId(null, item.getCid3())
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
        goodsList.forEach(goodsCWH -> {
            goodsCWH.getSpecs( ).forEach((k,v)->{
                System.out.println(k+"           "+v);
            });
        });
        goodsCWHRepository.saveAll(goodsList);
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

package com.cwh;

import com.cwh.common.utils.PageResult;
import com.cwh.dao.GoodsCWHRepository;
import com.cwh.pojo.Goods;
import com.cwh.pojo.GoodsCWH;
import com.cwh.pojo.Pages;
import com.cwh.service.impl.GoodsCwhServiceImpl;
import com.cwh.utils.HightLightUtil;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-10 15:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HgihtTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsCwhServiceImpl service;

    @Autowired
    private GoodsCWHRepository goodsCWHRepository;

    @Test
    public void testHigh(){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //条件
        builder.withQuery(QueryBuilders.matchQuery("all","华为"));
        //分页
        builder.withPageable(PageRequest.of(0,10));
        //高亮
        builder.withHighlightFields((
                new HighlightBuilder.Field("all")
                        .preTags("<font color='red'>")
                        .postTags("</font>")));
        Map<Long, String> all = HightLightUtil.getHignLigntMap(elasticsearchTemplate, builder.build(), Goods.class, "all");
        System.out.println(all.toString());
    }

    @Test
    public void sortTest(){
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("CPU核数","八核 + 微智核i6");
        PageResult<GoodsCWH> pageResult = service.searchGoods(new Pages("华为",1,20,false,null,null));
//        pageResult.getItems().forEach(i->{
//            System.out.println(i.getPrice() + "        "+i.getSkus());
//        });
    }

    @Test
    public void testss(){
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //条件
        builder.withQuery(QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("title","华为"))
                .should(QueryBuilders.matchQuery("brand","华为"))
                .should(QueryBuilders.matchQuery("category","华为"))

        );
        builder.addAggregation(AggregationBuilders.terms("品牌").field("specs.品牌.keyword"));
        AggregatedPage<GoodsCWH> search = (AggregatedPage<GoodsCWH>) goodsCWHRepository.search(builder.build());
        StringTerms aggregation = (StringTerms) search.getAggregation("品牌");
        System.out.println(aggregation.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString).collect(Collectors.toList()));

    }
}

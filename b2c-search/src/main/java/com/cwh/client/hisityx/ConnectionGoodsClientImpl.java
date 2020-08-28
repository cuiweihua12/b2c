package com.cwh.client.hisityx;

import com.cwh.client.ConnectionCategoryClient;
import com.cwh.entity.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-06 16:58
 */
//@Component
public class ConnectionGoodsClientImpl implements ConnectionCategoryClient {
    @Override
    public ResponseEntity<List<Category>> getCategoryByPid(Long pid) {
        return null;
    }

    @Override
    public ResponseEntity<String> removeCategoryById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<String> editCategoryById(Category category) {
        return null;
    }

    @Override
    public ResponseEntity<Category> saveCategory(Category category) {
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getCateGoryById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Category>> queryCategory(List<Long> ids) {
        return null;
    }

    @Override
    public ResponseEntity<List<Category>> queryCategoryAll() {
        return null;
    }
}

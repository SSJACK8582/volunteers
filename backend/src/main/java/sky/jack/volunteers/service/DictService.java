package sky.jack.volunteers.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sky.jack.volunteers.entity.Dict;
import sky.jack.volunteers.mapper.DictMapper;
import sky.jack.volunteers.tool.ExcelListener;
import sky.jack.volunteers.tool.Pagination;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class DictService {
    @Autowired
    DictMapper dictMapper;

    public int insertDict(Dict dict) {
        return dictMapper.insert(dict);
    }

    public int deleteDict(Long[] dictIds) {
        List<Long> dictIdList = Arrays.asList(dictIds);
        return dictMapper.deleteBatchIds(dictIdList);
    }

    public int updateDict(Dict dict) {
        return dictMapper.updateById(dict);
    }

    public Dict getDict(Long dictId) {
        return dictMapper.selectById(dictId);
    }

    public HashMap getDictByType(String dictType) {
        HashMap<String, String> hashMap = new HashMap<>();
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_type", dictType);
        List<Dict> dictList = dictMapper.selectList(queryWrapper);
        for (Dict dict : dictList) {
            hashMap.put(dict.getDictKey(), dict.getDictValue());
        }
        return hashMap;
    }

    public HashMap getDictByTypeList(List<String> dictTypeList) {
        HashMap<String, HashMap> result = new HashMap<>();
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("dict_type", dictTypeList);
        List<Dict> dictList = dictMapper.selectList(queryWrapper);
        for (Dict dict : dictList) {
            HashMap<String, String> hashMap = new HashMap<>();
            for (Dict d : dictList) {
                if (dict.getDictType().equals(d.getDictType())) {
                    hashMap.put(d.getDictKey(), d.getDictValue());
                }
            }
            result.put(dict.getDictType(), hashMap);
        }
        return result;
    }

    public List<Dict> getDictList() {
        return dictMapper.selectList(null);
    }

    public Page<Dict> getDictPage(Pagination pagination, Dict dict) {
        Page<Dict> page = new Page<>(pagination.getCurrent(), pagination.getSize());
        OrderItem orderItem = new OrderItem(pagination.getColumn(), pagination.isAsc());
        page.addOrder(orderItem);
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        if (dict != null) {
            if (dict.getDictName() != null && !"".equals(dict.getDictName())) {
                queryWrapper.like("dict_name", dict.getDictName());
            }
            if (dict.getDictType() != null && !"".equals(dict.getDictType())) {
                queryWrapper.like("dict_type", dict.getDictType());
            }
            if (dict.getDictKey() != null && !"".equals(dict.getDictKey())) {
                queryWrapper.like("dict_key", dict.getDictKey());
            }
            if (dict.getDictValue() != null && !"".equals(dict.getDictValue())) {
                queryWrapper.like("dict_value", dict.getDictValue());
            }
        }
        return dictMapper.selectPage(page, queryWrapper);
    }

    public int importDict(MultipartFile file) {
        int i = 0;
        try {
            ExcelListener listener = new ExcelListener();
            EasyExcel.read(file.getInputStream(), Dict.class, listener).sheet().doRead();
            List<Object> list = listener.getList();
            for (Object o : list) {
                i += insertDict((Dict) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}

package sky.jack.volunteers.service;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sky.jack.volunteers.entity.Notice;
import sky.jack.volunteers.mapper.NoticeMapper;
import sky.jack.volunteers.tool.ExcelListener;
import sky.jack.volunteers.tool.Pagination;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class NoticeService {
    private static final String WAIT = "0";
    private static final String FAIL = "1";
    @Autowired
    NoticeMapper noticeMapper;
    @Autowired
    UserService userService;

    public int insertNotice(Notice notice) {
        Long userId = userService.getUserInfo().getUserId();
        notice.setUserId(userId);
        return noticeMapper.insert(notice);
    }

    public int deleteNotice(Long[] noticeIds) {
        List<Long> noticeIdList = Arrays.asList(noticeIds);
        return noticeMapper.deleteBatchIds(noticeIdList);
    }

    public int updateNotice(Notice notice) {
        return noticeMapper.updateById(notice);
    }

    public int verifyNotice(Long[] noticeIds, String status) {
        List<Long> noticeIdList = Arrays.asList(noticeIds);
        Notice notice = new Notice();
        notice.setNoticeStatus(status);
        UpdateWrapper<Notice> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("notice_id", noticeIdList);
        return noticeMapper.update(notice, updateWrapper);
    }

    public Notice getNotice(Long noticeId) {
        return noticeMapper.selectById(noticeId);
    }

    public List<Notice> getNoticeList() {
        return noticeMapper.selectList(null);
    }

    public List<Notice> getBannerList() {
        Page<Notice> page = new Page<>(1, 3);
        OrderItem orderItem = new OrderItem("create_time", false);
        page.addOrder(orderItem);
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.notIn("notice_status", Arrays.asList(WAIT, FAIL));
        return noticeMapper.selectPage(page, queryWrapper).getRecords();
    }

    public Page<Notice> getNoticePage(Pagination pagination, Notice notice) {
        List<String> roleList = StpUtil.getRoleList();
        Page<Notice> page = new Page<>(pagination.getCurrent(), pagination.getSize());
        OrderItem orderItem = new OrderItem(pagination.getColumn(), pagination.isAsc());
        page.addOrder(orderItem);
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        if (roleList.isEmpty() || roleList.contains("volunteer")) {
            queryWrapper.notIn("notice_status", Arrays.asList(WAIT, FAIL));
        }
        if (notice != null) {
            if (notice.getTitle() != null && !"".equals(notice.getTitle())) {
                queryWrapper.like("title", notice.getTitle());
            }
            if (notice.getContent() != null && !"".equals(notice.getContent())) {
                queryWrapper.like("content", notice.getContent());
            }
            if (notice.getNoticeType() != null && !"".equals(notice.getNoticeType())) {
                queryWrapper.eq("notice_type", notice.getNoticeType());
            }
        }
        return noticeMapper.selectPage(page, queryWrapper);
    }

    public int importNotice(MultipartFile file) {
        int i = 0;
        try {
            ExcelListener listener = new ExcelListener();
            EasyExcel.read(file.getInputStream(), Notice.class, listener).sheet().doRead();
            List<Object> list = listener.getList();
            for (Object o : list) {
                i += insertNotice((Notice) o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}

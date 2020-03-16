package club.nipengfei.ssm.service.impl;

import club.nipengfei.ssm.dao.IOrdersDao;
import club.nipengfei.ssm.domain.Orders;
import club.nipengfei.ssm.service.IOrdersService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class OrdersServiceImpl implements IOrdersService {

    @Autowired
    private IOrdersDao ordersDao;

    @Override
    public List<Orders> findAll(int page, int size) throws Exception {
        // pageNum是页码值，pageSize是每页显示条数
        PageHelper.startPage(page,size);
        return ordersDao.findAll();
        /*List<Orders> ordersList = ordersDao.findAll();
        System.out.println(Arrays.toString(ordersList.toArray()));
        return ordersList;*/
    }

    @Override
    public Orders findById(int ordersId) throws Exception {
        return ordersDao.findById(ordersId);
    }
}

package club.nipengfei.ssm.service;

import club.nipengfei.ssm.domain.Orders;

import java.util.List;

public interface IOrdersService {

    List<Orders> findAll(int page, int size) throws Exception;

    Orders findById(int ordersId) throws Exception;
}

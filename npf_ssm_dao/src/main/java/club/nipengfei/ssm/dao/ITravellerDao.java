package club.nipengfei.ssm.dao;

import club.nipengfei.ssm.domain.Traverller;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ITravellerDao {

    @Select("select * from traveller where id in (select travellerId from order_traveller where orderId=#{ordersId})")
    public List<Traverller> findByOrdersId(int ordersId) throws Exception;
}

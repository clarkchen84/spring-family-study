package sizhe.chen.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;
import sizhe.chen.mybatis.model.Coffee;

import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 9:25 下午 2022/4/10
 * @Description:
 * @Modified:
 * @Version:
 */
@Mapper
public interface CoffeeMapper {
    @Select("select * from t_coffee order by id")
    List<Coffee> findAllWithRowBounds(RowBounds rowBounds);

    @Select("select * from t_coffee order by id")
    List<Coffee> findAllWithParams(@Param("pageNum") int pageNum,
                                      @Param("pageSize") int pageSize);
}

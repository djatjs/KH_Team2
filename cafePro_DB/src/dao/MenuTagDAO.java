package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import model.vo.Menu_Tag;


public interface MenuTagDAO {

	boolean insertMenuTag(@Param("meCode")String meCode, @Param("tagNum")int tagNum);

	boolean deleteMenuTag(@Param("meCode")String meCode, @Param("tagNum")int tagNum);

	
	
	
	

}

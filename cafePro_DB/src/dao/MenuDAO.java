package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.vo.Menu;



public interface MenuDAO {
	
	Menu selectMenuByNameAndHI(@Param("menu")Menu menu);

	boolean insertMenu(@Param("meCode")String caCode, @Param("menu")Menu menu);

	List<Menu> selectAllMenu();

	Menu selectMenuByCode(String meCode);
			
	boolean deleteMenu(int meCode);

	Menu selectMenu(
			@Param("meName")String meName, 
			@Param("mePrice")int mePrice);

	boolean menuExists_HI(@Param("meHotIce")String meHotIce);

	boolean updateMenu(@Param("menu")Menu menu);

	boolean deleteMenu(String meCode);


	Menu selectMenuByNumAndCode(@Param("meCaNum")int meCaNum, @Param("meCode")String meCode);

	boolean menuExists(@Param("meName")String meName, @Param("meHotIce")String meHotIce);

	

	

}

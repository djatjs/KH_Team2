package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import model.vo.Tag;


public interface TagDAO {
	
	Tag selectTag(@Param("tag") Tag tag);
	
	boolean insertTag(@Param("tag")Tag tag);

	List<Tag> selectAllTag();
	
	Tag selectTagByNum(int tagNum);
	
	boolean updateTag(@Param("tag")Tag dbTag, @Param("tagName")String newTagName);

	boolean deleteTag(@Param("tag")Tag dbTag);

	Tag selectTagByName(@Param("tagName")String tagName);

	boolean insertMenuTag(
			@Param("menuCode")String menuCode, 
			@Param("tagNum")int tagNum);

	

}

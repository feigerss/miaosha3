package com.miaoshaproject.dao;

import com.miaoshaproject.dataobject.UserDO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */
    int insert(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */
    int insertSelective(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */
    UserDO selectByPrimaryKey(Long id);
    /**
     * This method was generated by MyBatis wpfei.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */

    UserDO selectByTelphone(String telphone);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */
    int updateByPrimaryKeySelective(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */
    int updateByPrimaryKey(UserDO record);
}
package org.fai.study.projectsem4.repository;

import org.fai.study.projectsem4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

    @Query(value = "SELECT u.user_id, " +
            "u.full_name, " +
            "u.phone_number, " +
            "u.email, " +
            "u.avatar, " +
            "CASE " +
            "WHEN EXISTS ( " +
            "SELECT 1 FROM shippings sh " +
            "WHERE sh.shipper_id = u.user_id AND sh.status IN ('RECEIVED', 'SHIPPING') " +
            ") THEN 'NOT_AVAILABLE' " +
            "ELSE 'AVAILABLE' " +
            "END AS shipperStatus " +
            "FROM users u " +
            "JOIN user_roles ur ON u.user_id = ur.user_id " +
            "WHERE ur.role_id = 3",
            nativeQuery = true)
    List<Object[]> getAllShipperInforWithStatus();

    @Query(value = "select u.user_id,u.address,u.email,u.phone_number,u.full_name,u.avatar,r.name from users u " +
            "join user_roles ur on u.user_id = ur.user_id " +
            "join roles r on ur.role_id = r.role_id where r.name = 'ROLE_USER'",nativeQuery = true)
    List<Object[]> getAllUserInforWithRoles();

    @Query(value = "select r.name,count(u.user_id) as totalUser from users u " +
            "join user_roles ur on u.user_id = ur.user_id " +
            "join roles r on ur.role_id = r.role_id " +
            "group by r.name",nativeQuery = true)
    List<Object[]> countNumberOfUserByRole();


}

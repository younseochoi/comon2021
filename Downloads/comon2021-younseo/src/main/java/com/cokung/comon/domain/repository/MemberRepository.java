package com.cokung.comon.domain.repository;

import com.cokung.comon.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

//    @Query(value = "SELECT * FROM member WHERE member.id = :id",nativeQuery = true)
//    Member findById(@Param("id") String id);
    Optional<Member> findById(String id);
}

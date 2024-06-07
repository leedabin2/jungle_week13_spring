package com.jungle.week13.member.repository;

import com.jungle.week13.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member , Long> {
    Optional<Member> findByUsername(String username);
}

package com.quickstarts.kitchensink.repository;

import com.quickstarts.kitchensink.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MemberRepository extends MongoRepository<Member, String> {
    List<Member> findAllByOrderByNameAsc();

    Member findByEmail(String email);
}

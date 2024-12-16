package com.quickstarts.kitchensink.repository;

import com.quickstarts.kitchensink.model.Member;
import com.quickstarts.kitchensink.util.MongoTestingUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
class MemberRepositoryTest extends MongoTestingUtil {

    @Autowired
    private MemberRepository MemberRepository;

    @AfterEach
    void cleanUp() {
        MemberRepository.deleteAll();
    }

    @Test
    void testSaveAndFindById() {
        // Create a new Member
        Member member = new Member();
        member.setId("999");
        member.setName("Akhil");
        member.setEmail("Akhil@email.com");
        member.setPhoneNumber("1234567890");

        // Save the member
        MemberRepository.save(member);

        // Retrieve the member by ID
        Optional<Member> retrievedMember = MemberRepository.findById("999");

        // Verify the member details
        assertThat(retrievedMember).isPresent();
        assertThat(retrievedMember.get().getName()).isEqualTo("Akhil");
        assertThat(retrievedMember.get().getEmail()).isEqualTo("Akhil@email.com");
        assertThat(retrievedMember.get().getPhoneNumber()).isEqualTo("1234567890");
    }

    @Test
    void testNotFound() {
        // Attempt to retrieve a non-existent member by ID
        Optional<Member> retrievedMember = MemberRepository.findById("non-existent-id");

        // Verify the member is not found
        assertThat(retrievedMember).isNotPresent();
    }

    @Test
    void testFindAllByOrderByNameAsc() {
        // Create and save multiple Members
        Member member1 = new Member();
        member1.setId("1");
        member1.setName("bhatia");
        member1.setEmail("alice@example.com");
        member1.setPhoneNumber("1234567890");
        MemberRepository.save(member1);

        Member member2 = new Member();
        member2.setId("2");
        member2.setName("testing");
        member2.setEmail("bob@example.com");
        member2.setPhoneNumber("0987654321");
        MemberRepository.save(member2);

        // Retrieve all members ordered by name ascending
        List<Member> members = MemberRepository.findAllByOrderByNameAsc();

        // Verify the order of members
        assertThat(members).hasSize(2);
        assertThat(members.get(0).getName()).isEqualTo("bhatia");
        assertThat(members.get(1).getName()).isEqualTo("testing");
    }
}
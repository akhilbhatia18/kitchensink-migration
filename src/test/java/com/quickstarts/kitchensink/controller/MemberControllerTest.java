package com.quickstarts.kitchensink.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickstarts.kitchensink.repository.MemberRepository;
import com.quickstarts.kitchensink.model.Member;
import com.quickstarts.kitchensink.util.MongoTestingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.quickstarts.kitchensink.util.JwtTestUtil;
@SpringBootTest
@AutoConfigureMockMvc
@Disabled
class MemberControllerTest extends MongoTestingUtil {
     @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    void shouldRegisterMember() throws Exception {
        Member member = new Member();
        member.setName("Akhil");
        member.setEmail("this@gmail.com");
        member.setPhoneNumber("1234567890");

        String jwtToken = JwtTestUtil.generateToken("user");

        mockMvc.perform(post("/members")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Akhil"))
                .andExpect(jsonPath("$.email").value("this@gmail.com"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldGetAllMembers() throws Exception {
        Member member1 = new Member();
        member1.setName("Akhil");
        member1.setEmail("talk@gmail.com");
        member1.setPhoneNumber("1234567890");

        Member member2 = new Member();
        member2.setName("Bhatia");
        member2.setEmail("Bhatia@gmail.com");
        member2.setPhoneNumber("1234567890");

        memberRepository.save(member1);
        memberRepository.save(member2);
        String jwtToken = JwtTestUtil.generateToken("user");
        mockMvc.perform(get("/members").header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Akhil"))
                .andExpect(jsonPath("$[1].name").value("Bhatia"));
    }

    @Test
    void shouldGetMemberById() throws Exception {
        Member member = new Member();
        member.setName("Set");
        member.setEmail("akhil@gmail.com");
        Member savedMember = memberRepository.save(member);
        String jwtToken = JwtTestUtil.generateToken("user");

        mockMvc.perform(get("/members/{id}", savedMember.getId()).header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Set"))
                .andExpect(jsonPath("$.email").value("akhil@gmail.com"));
    }

    @Test
    void shouldReturn404WhenMemberNotFound() throws Exception {
        String jwtToken = JwtTestUtil.generateToken("user");
        mockMvc.perform(get("/members/{id}", "9999").header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }



    @Test
    void shouldRetur409WhenEmailIsDuplicate() throws Exception {
        Member member1 = new Member();
        member1.setName("Set");
        member1.setEmail("set@gmail.com");
        member1.setPhoneNumber("1234567890");

        Member member2 = new Member();
        member2.setName("Set1");
        member2.setEmail("set@gmail.com");
        member2.setPhoneNumber("0987654321");
        String jwtToken = JwtTestUtil.generateToken("user");
        mockMvc.perform(post("/members")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member2)))
                .andExpect(status().isBadRequest());
    }

}
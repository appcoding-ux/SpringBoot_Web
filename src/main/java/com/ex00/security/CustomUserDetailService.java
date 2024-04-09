package com.ex00.security;

import com.ex00.domain.Member;
import com.ex00.repository.MemberRepository;
import com.ex00.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("login username :" + username);

        Optional<Member> result = memberRepository.getWithRoles(username);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("username not found...");
        }

        Member member = result.get();

        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(member.getMid(), member.getMpw(), member.getEmail(), member.isDel(), false, member.getRoleSet().stream().map(memberRole ->
            new SimpleGrantedAuthority("ROLE_" + memberRole.name())
        ).collect(Collectors.toList()));

        log.info("memberSecurityDTO" + memberSecurityDTO);

        return memberSecurityDTO;
    }
}

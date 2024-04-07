package com.ex00.service;

import com.ex00.dto.PageRequestDTO;
import com.ex00.dto.PageResponseDTO;
import com.ex00.dto.ReplyDTO;

public interface ReplyService {

    Long insert(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
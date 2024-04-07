package com.ex00.service;

import com.ex00.dto.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
class BoardServiceImplTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }

    @Test
    public void testInsert(){
        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        Long bno = boardService.insert(boardDTO);

        log.info(bno);
    }

    @Test
    public void testInsertWithImages(){
        BoardDTO boardDTO = BoardDTO.builder()
                .title("File...Sample...Title")
                .content("Sample Content")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID().toString()+"_aaa.jpg",UUID.randomUUID().toString()+"_aaa.jpg",UUID.randomUUID().toString()+"_aaa.jpg"
                ));

        Long bno = boardService.insert(boardDTO);

        log.info(bno);
    }

    @Test
    public void testReadAll(){
        Long bno = 101L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for(String fileName : boardDTO.getFileNames()){
            log.info(fileName);
        }
    }   

    @Test
    public void testModify(){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("Update...101")
                .content("updated content 101")
                .build();

        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID().toString()+"_zzz.jpg"));

        boardService.modify(boardDTO);
    }

    @Test
    public void testRemoveAll(){
        Long bno = 1L;

        boardService.remove(bno);
    }

    @Test
    public void testListWithAll(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno() + ":" + boardListAllDTO.getTitle());

            if(boardListAllDTO.getBoardImages() != null){
                for(BoardImageDTO boardImageDTO : boardListAllDTO.getBoardImages()){
                    log.info(boardImageDTO);
                }
            }

            log.info("--------------------");
        });
    }

}
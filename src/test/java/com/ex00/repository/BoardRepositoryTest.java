package com.ex00.repository;

import com.ex00.domain.Board;
import com.ex00.domain.BoardImage;
import com.ex00.dto.BoardDTO;
import com.ex00.dto.BoardListAllDTO;
import com.ex00.dto.BoardListReplyCountDTO;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
@Log4j2
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert(){
        for(int i = 1; i <= 100; i++){
            Board board = Board.builder()
                    .title("title.." + i)
                    .content("content..." + i)
                    .writer("user"+ (i%10))
                    .build();
            Board result = boardRepository.save(board);
            log.info(result.getBno());
        }

    }

    @Test
    public void testSelect(){
        Long bno = 100L;

        Board board = boardRepository.findById(bno).orElseThrow();

        log.info(board);
    }

    @Test
    public void testPaging(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);

        result.forEach(board -> log.info(board));

        log.info(result.getTotalElements());
        log.info(result.getTotalPages());
        log.info(result.getNumber());
        log.info(result.getSize());
    }

    @Test
    public void testSearch1(){
        Pageable pageable = PageRequest.of(1, 10,  Sort.by("bno").descending());

        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll(){
        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        log.info(result.getTotalPages());

        log.info(result.getSize());

        log.info(result.getNumber());

        log.info(result.hasPrevious() + ":" + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testSearchReplyCount(){
        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        log.info(result.getTotalPages());

        log.info(result.getSize());

        log.info(result.getNumber());

        log.info(result.hasPrevious() + ": " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testInsertWithImages(){
        Board board = Board.builder()
                .title("Image Test")
                .content("첨부파일 테스트")
                .writer("tester")
                .build();

        for(int i = 0; i < 3; i++){
            board.addImage(UUID.randomUUID().toString(),  "file"+i+".jpg");
        }

        boardRepository.save(board);
    }

    @Test
    public void testReadWithImages(){
        Board board = boardRepository.findByIdWithImages(1L).orElseThrow();

        log.info(board);

        log.info("----------------------");

        for(BoardImage boardImage : board.getImageSet()){
            log.info(boardImage);
        }
    }

    @Transactional
    @Commit
    @Test
    public void testModifyImages(){
        Board board = boardRepository.findByIdWithImages(1L).orElseThrow();

        board.clearImages();

        for(int i = 0; i < 2; i++){
            board.addImage(UUID.randomUUID().toString(), "updataFile"+i+".jpg");
        }

        boardRepository.save(board);
    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll(){
        Long bno = 1L;

        replyRepository.deleteByBoard_Bno(bno);

        boardRepository.deleteById(bno);
    }

    @Test
    public void testInsertAll(){
        for(int i = 0; i <= 100; i++){
            Board board = Board.builder()
                    .title("Title.."+i)
                    .content("Content.."+i)
                    .writer("writer.."+i)
                    .build();

            for(int j = 0; j < 3; j++){
                if(i % 5 == 0){
                    continue;
                }
                board.addImage(UUID.randomUUID().toString(), i+"file"+j+".jpg");
            }
            boardRepository.save(board);
        }
    }

    @Transactional
    @Test
    public void testSearchImageReplyCount(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

//        boardRepository.searchWithAll(null, null, pageable);

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);

        log.info("-------------------");
        log.info(result.getTotalElements());

        result.getContent().forEach(board -> log.info(board));
    }


}
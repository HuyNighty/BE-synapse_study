package com.synapse.study.service;

import com.synapse.study.dto.response.PostResponse;
import com.synapse.study.entity.Bookmark;
import com.synapse.study.entity.Post;
import com.synapse.study.entity.User;
import com.synapse.study.enums.ErrorCode;
import com.synapse.study.exception.AppException;
import com.synapse.study.mapper.PostMapper;
import com.synapse.study.repository.BookmarkRepository;
import com.synapse.study.repository.PostRepository;
import com.synapse.study.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookmarkServiceImpl implements BookmarkService {

    BookmarkRepository bookmarkRepository;
    PostRepository postRepository;
    UserRepository userRepository;

    PostMapper postMapper;

    @Override
    @Transactional
    public String toggleBookmark(UUID postId) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Bookmark.BookmarkKey key = new Bookmark.BookmarkKey(user.getId(), postId);

        if (bookmarkRepository.existsById(key)) {
            bookmarkRepository.deleteById(key);
            return "Unbookmarked";
        } else {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

            Bookmark bookmark = Bookmark.builder()
                    .id(key)
                    .user(user)
                    .post(post)
                    .build();
            bookmarkRepository.save(bookmark);
            return "Bookmarked";
        }
    }

    @Override
    public Page<PostResponse> getMyBookmarks(Pageable pageable) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        Page<Bookmark> bookmarks = bookmarkRepository.findByUserIdOrderByCreatedAtDesc(UUID.fromString(userId), pageable);
        return bookmarks.map(bookmark -> postMapper.toPostResponse(bookmark.getPost()));
    }
}
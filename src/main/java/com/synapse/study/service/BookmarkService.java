package com.synapse.study.service;

import com.synapse.study.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BookmarkService {

    String toggleBookmark(UUID postId);

    Page<PostResponse> getMyBookmarks(Pageable pageable);
}

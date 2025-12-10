package com.synapse.study.service;

import com.synapse.study.entity.Asset;
import org.springframework.web.multipart.MultipartFile;

public interface AssetService {

    Asset uploadFile(MultipartFile file);
}

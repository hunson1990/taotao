package com.taotao.manager.service;

import java.util.Map;

public interface PictureService {
    Map uploadPicture(String oldName, byte[] bytes);
}

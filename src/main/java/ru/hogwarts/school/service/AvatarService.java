package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;

@Service
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Autowired
    private AvatarRepository avatarRepository;

    public Page<Avatar> getAvatars(Pageable pageable) {
        logger.info("Вызван метод получения аватаров с пагинацией");
        return avatarRepository.findAll(pageable);
    }
}
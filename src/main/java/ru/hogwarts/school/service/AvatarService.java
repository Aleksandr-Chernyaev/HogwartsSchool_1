package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;

@Service
public class AvatarService {

    @Autowired
    private AvatarRepository avatarRepository;

    public Page<Avatar> getAvatars(Pageable pageable) {
        return avatarRepository.findAll(pageable);
    }
}
package ch.supsi.webapp.web.service;

import ch.supsi.webapp.web.model.Type;
import ch.supsi.webapp.web.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    @Autowired private TypeRepository typeRepository;

    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }
}

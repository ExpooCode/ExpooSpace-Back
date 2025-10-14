package ExpooCode.ExpooCode.persistence.dao;

import ExpooCode.ExpooCode.persistence.entity.Extra;
import ExpooCode.ExpooCode.persistence.repository.ExtraRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ExtraDao {

    private final ExtraRepository extraRepository;

    public ExtraDao(ExtraRepository extraRepository) {
        this.extraRepository = extraRepository;
    }

    public List<Extra> findAll() {
        return extraRepository.findAll();
    }

    public Optional<Extra> findById(Integer id) {
        return extraRepository.findById(id);
    }

    public Extra save(Extra extra) {
        return extraRepository.save(extra);
    }

    public void delete(Extra extra) {
        extraRepository.delete(extra);
    }

    public void deleteById(Integer id) {
        extraRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return extraRepository.existsById(id);
    }
}

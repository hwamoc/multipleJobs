package co.kr.talentx.openscout.service;

import co.kr.talentx.openscout.entity.Income;
import co.kr.talentx.openscout.helper.CSVHelper;
import co.kr.talentx.openscout.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class IncomeService {
    @Autowired
    IncomeRepository repository;

    public void save(MultipartFile file) {
        try {
            List<Income> incomes = CSVHelper.csvToIncomeData(file.getInputStream());
            repository.saveAll(incomes);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Income> getAllIncomeRaws() {
        return repository.findAll();
    }
}

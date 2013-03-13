package com.senacor.hd12.neo.engine;

import com.senacor.hd12.neo.model.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 16.06.12
 * Time: 14:28
 */
public class OrganisationRepositoryImpl implements OrganisationRepositoryExtension {

    @Autowired
    private OrganisationRepository baseRepository;

    @Override
    @Transactional
    public boolean deleteOrganisation(String name) {
        Organisation o = baseRepository.getOrganisationByName(name);
        if (null == o) {
            return false;
        }

        baseRepository.delete(o);
        return true;
    }
}

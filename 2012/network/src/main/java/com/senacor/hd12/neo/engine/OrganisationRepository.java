package com.senacor.hd12.neo.engine;

import com.senacor.hd12.neo.model.Organisation;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 16.06.12
 * Time: 09:55
 */
public interface OrganisationRepository extends GraphRepository<Organisation>, NamedIndexRepository<Organisation>,
        OrganisationRepositoryExtension {

    Organisation getOrganisationByName(String name);
}

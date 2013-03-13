package com.senacor.hd12.neo.engine;

/**
 * Created with IntelliJ IDEA.
 * User: akeefer
 * Date: 16.06.12
 * Time: 12:48
 */
public interface OrganisationRepositoryExtension {

    /**
     * Deletes a Organisation by name
     *
     * @param name Organisation name
     * @return if the Organisation was successfully deleted
     */
    boolean deleteOrganisation(String name);
}

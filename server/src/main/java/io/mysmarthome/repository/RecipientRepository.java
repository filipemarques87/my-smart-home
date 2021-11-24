package io.mysmarthome.repository;

import io.mysmarthome.model.entity.Recipient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipientRepository extends CrudRepository<Recipient, String> {

    List<Recipient> findByAddress(String address);
}

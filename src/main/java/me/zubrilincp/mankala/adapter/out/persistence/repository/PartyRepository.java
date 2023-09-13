package me.zubrilincp.mankala.adapter.out.persistence.repository;

import java.util.UUID;
import me.zubrilincp.mankala.adapter.out.persistence.entity.PartyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<PartyEntity, UUID> {}

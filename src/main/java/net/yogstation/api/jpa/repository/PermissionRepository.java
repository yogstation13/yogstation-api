package net.yogstation.api.jpa.repository;

import net.yogstation.api.jpa.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface PermissionRepository extends JpaRepository {
    Stream<PermissionEntity> findByUserGroupInOrUser(List<Integer> groupIds, int user);
}

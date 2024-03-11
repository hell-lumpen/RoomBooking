package org.mai.roombooking.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;

    public List<Group> saveGroups(@NonNull List<Group> groups) {
        List<Group> res =  groups.stream().map(this::saveGroup).toList();
        log.info("UPDATE SCHEDULE. Saved " + res.size() + " groups");
        return res;
    }

    /**
     * Метод сохранения/обновления данных учебной группы
     * @param group учебная группа
     */
    public Group saveGroup(@NonNull Group group) {
        var existingGroup = groupRepository.findByName(group.getName());
        existingGroup.ifPresent(value -> group.setId(value.getId()));
        return groupRepository.save(group);
    }
}

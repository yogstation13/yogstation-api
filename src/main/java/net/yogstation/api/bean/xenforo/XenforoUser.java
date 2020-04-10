package net.yogstation.api.bean.xenforo;

import lombok.Data;

import java.util.List;

@Data
public class XenforoUser {
    private int userGroupId;
    private List<Integer> secondaryGroupIds;
    private String username;
    private int userId;
}

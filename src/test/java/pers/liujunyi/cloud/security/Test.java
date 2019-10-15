package pers.liujunyi.cloud.security;

import org.springframework.security.core.GrantedAuthority;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] ars) {
        List<StaffOrg> list = new ArrayList<>();

        StaffOrg staffOrg1 = new StaffOrg();
        staffOrg1.setOrgId(1L);
        list.add(staffOrg1);

        StaffOrg staffOrg2 = new StaffOrg();
        staffOrg2.setOrgId(1L);
        list.add(staffOrg2);

        StaffOrg staffOrg3 = new StaffOrg();
        staffOrg3.setOrgId(3L);
        list.add(staffOrg3);

        List<Long> orgIds = list.stream().map(StaffOrg::getOrgId).distinct().collect(Collectors.toList());
        orgIds.stream().forEach(item -> {

            System.out.println(item);
        });


        Set<GrantedAuthority> grantedAuths = SecurityConstant.grantedAuths("[{\"authority\":\"ROLE_ADMIN\"}]");
        for (GrantedAuthority authority : grantedAuths) {
            System.out.println(authority.getAuthority());
        }
    }
}

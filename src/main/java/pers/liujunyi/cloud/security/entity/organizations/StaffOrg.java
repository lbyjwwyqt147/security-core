package pers.liujunyi.cloud.security.entity.organizations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.elasticsearch.annotations.Document;
import pers.liujunyi.cloud.common.entity.BaseEntity;

import javax.persistence.Entity;

/***
 * 文件名称: StaffOrg.java
 * 文件描述: 职工关联组织机构
 * 公 司:
 * 内容摘要:
 *
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document(indexName = "photo_manage_staff_org", type = "staffOrg", shards = 1, replicas = 0)
@DynamicInsert
@DynamicUpdate
public class StaffOrg extends BaseEntity {

    private static final long serialVersionUID = 7025563314086782146L;
    /** 机构ID */
    private Long orgId;

    /** 职工id */
    private Long staffId;

    /** 状态：0：正常  1：禁用 */
    private Byte status;

    private Long dataVersion;
}
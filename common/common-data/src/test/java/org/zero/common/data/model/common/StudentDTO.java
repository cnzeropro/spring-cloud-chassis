package org.zero.common.data.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2023/1/5
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"createBy", "updateBy"})
public class StudentDTO extends StudentPO {

}

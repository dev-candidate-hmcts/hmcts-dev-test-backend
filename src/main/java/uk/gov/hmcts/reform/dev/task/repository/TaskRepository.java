package uk.gov.hmcts.reform.dev.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.hmcts.reform.dev.task.model.Task;


public interface TaskRepository extends JpaRepository<Task,Long> {

}

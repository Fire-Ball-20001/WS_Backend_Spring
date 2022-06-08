package org.backend.spring.actions;

import lombok.AllArgsConstructor;
import org.backend.spring.actions.filters.Filter;
import org.backend.spring.models.Employee;
import org.backend.spring.models.PostEmployee;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class FilterEmployeeAction implements BinaryAction<Employee, Filter<Employee>,Boolean> {

    private final Action<UUID, PostEmployee> getPostAction;

    @Override
    public Boolean execute(Employee object, Filter<Employee> filter) {
        boolean result = filter.match(object);
        if(filter.getSubFilter() != null)
        {
            try {
                result = ((Filter<PostEmployee>) filter.getSubFilter()).match(getPostAction.execute(object.getPostId()));
            } catch (Exception e)
            {
                return result;
            }
        }
        return result;
    }

    @Override
    public Boolean execute(Employee object, Filter<Employee> filter, boolean isStrictly) {
        boolean result = false;
        if(isStrictly)
        {
            result = filter.matchStrictly(object);
            if(filter.getSubFilter() != null)
            {
                try {
                    result = ((Filter<PostEmployee>) filter.getSubFilter()).matchStrictly(getPostAction.execute(object.getPostId()));
                } catch (Exception e)
                {
                    return result;
                }
            }
        }
        else
        {
            result = filter.matchApproximately(object);
            if(filter.getSubFilter() != null)
            {
                try {
                    result = ((Filter<PostEmployee>) filter.getSubFilter()).matchApproximately(getPostAction.execute(object.getPostId()));
                } catch (Exception e)
                {
                    return result;
                }
            }
        }

        return result;
    }
}

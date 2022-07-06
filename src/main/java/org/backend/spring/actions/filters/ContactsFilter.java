package org.backend.spring.actions.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.backend.spring.models.Contacts;

import static org.backend.spring.utils.BooleanUtils.*;

@AllArgsConstructor
@Builder
@Getter
public class ContactsFilter implements Filter<Contacts>{
    private String phone;
    private String email;
    private String workEmail;
    private boolean isStrictly = false;
    @Override
    public boolean matchStrictly(Contacts object) {
        if(phone != null)
        {
            if(!object.getPhone().equals(phone))
            {
                return false;
            }
        }
        if(email != null)
        {
            if(!object.getEmail().equals(email))
            {
                return false;
            }
        }
        if(workEmail != null)
        {
            return object.getWorkEmail().equals(workEmail);
        }
        return true;
    }

    @Override
    public boolean matchApproximately(Contacts object) {
        if(phone != null)
        {
            if(!isApproximatelyMatch(object.getPhone(),phone))
            {
                return false;
            }
        }
        if(email != null)
        {
            if(!isApproximatelyMatch(object.getEmail(),email))
            {
                return false;
            }
        }
        if(workEmail != null)
        {
            return isApproximatelyMatch(object.getWorkEmail(),workEmail);
        }
        return true;
    }

    @Override
    public boolean match(Contacts object) {
        if(isStrictly)
        {
            return matchStrictly(object);
        }
        else
        {
            return matchApproximately(object);
        }
    }
}

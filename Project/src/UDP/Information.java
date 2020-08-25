package UDP;

public class Information
{
    private String date;
    private String time;
    private String eventContext;
    private String component;
    private String eventName;
    private String description;
    private String origin;
    private String ipAddress;

    public Information()
    {
        this (null, null, null, null, null, null, null, null);
    }

    public Information (String date, String time, String eventContext, String component, String eventName, String description, String origin, String ipAddress)
    {
        setDate(date);
        setTime(time);
        setEventContext(eventContext);
        setComponent(component);
        setEventName(eventName);
        setDescription(description);
        setOrigin(origin);
        setIpAddress(ipAddress);
    }

    public Information (Information information)
    {
        this(information.getDate(), information.getTime(), information.getEventContext(), information.getComponent(), information.getEventName(), information.getDescription(), information.getOrigin(), information.getIpAddress());
    }

    public void setDate (String date)
    {
        if (date != null)
        {
            this.date = date;
        }
        else
        {
            this.date = "1/1/18";
        }
    }

    public void setTime (String time)
    {
        if (time != null)
        {
            this.time = time;
        }
        else
        {
            this.time = "00:00";
        }
    }

    public void setEventContext (String eventContext)
    {
        if (eventContext != null)
        {
            this.eventContext = eventContext;
        }
        else
        {
            this.eventContext = "event";
        }
    }

    public void setComponent (String component)
    {
        if (component != null)
        {
            this.component = component;
        }
        else
        {
            this.component = "component";
        }
    }

    public void setEventName (String eventName)
    {
        if (eventName != null)
        {
            this.eventName = eventName;
        }
        else
        {
            this.eventName = "activity";
        }
    }

    public void setDescription (String description)
    {
        if (description != null)
        {
            this.description = description;
        }
        else
        {
            this.description = "description";
        }
    }

    public void setOrigin (String origin)
    {
        if (origin != null)
        {
            this.origin = origin;
        }
        else
        {
            this.origin = "origin";
        }
    }

    public void setIpAddress (String ipAddress)
    {
        if (ipAddress != null)
        {
            this.ipAddress = ipAddress;
        }
        else
        {
            this.ipAddress = "0.0.0.0";
        }
    }

    public String getDate()
    {
        return this.date;
    }

    public String getTime()
    {
        return this.time;
    }

    public String getEventContext()
    {
        return this.eventContext;
    }

    public String getComponent()
    {
        return this.component;
    }

    public String getEventName()
    {
        return this.eventName;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getOrigin()
    {
        return this.origin;
    }

    public String getIpAddress()
    {
        return this.ipAddress;
    }

    @Override
    public String toString()
    {
        return String.format("INFORMATION\nDate: %s\nTime: %s\nEvent context: %s\nComponent: %s\nEvent name: %s\nDescription: %s\nOrigin: %s\nIP address: %s\n",
                this.date,
                this.time,
                this.eventContext,
                this.component,
                this.eventName,
                this.description,
                this.origin,
                this.ipAddress);
    }
}

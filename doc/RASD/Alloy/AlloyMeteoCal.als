//SIGNATURE
sig RegisteredUser{
calendar:one Calendar
}

sig Invitation{
Guest: some RegisteredUser,
event:one Event
}
sig WeatherConstrain{
}
sig Calendar{
}

sig Event{
 Owner:one RegisteredUser,
 constrain:one WeatherConstrain
}
//FACTS
fact calendarConstrain{
//the number of Calendar must be equals to the RegisteredUser
#Calendar=#RegisteredUser
//two or more RegisteredUser cannot refer to the same calendar
no disj r1,r2:RegisteredUser | r1.calendar=r2.calendar
}
fact eventConstrain{
//the number of Invitation must be equals to Event
#Event=#Invitation
//each event refer to a different weather constrain
#WeatherConstrain=#Event
no disj e1,e2:Event | e1.constrain=e2.constrain
}
fact invitationConstrain{
//two or more Invitation cannot refer to the same event 
no disj r,p:Invitation | p.event=r.event
//the owner of an event cannot receive the invitation of its event
all  disj e:Event , i1: Invitation | i1.event=e implies e.Owner not in i1.Guest
}


//ASSERTION

assert oneCalendarforUser{
no disj r1,r2:RegisteredUser | r1.calendar=r2.calendar
}
//check oneCalendarforUser
assert  ownerNotReceveir{
all  disj e:Event , i1: Invitation | i1.event=e implies e.Owner not in i1.Guest 
}
//check ownerNotReceveir
assert oneInvitationforEvent{
no disj r,p:Invitation | p.event=r.event and p.Guest = r.Guest
}
//check oneInvitationforEvent
assert onecal{
#Calendar=#RegisteredUser
}
//check onecal
assert oneConstrain{
no disj e1,e2:Event | e1.constrain=e2.constrain
}
//check oneConstrain
pred show(){} 
run show 

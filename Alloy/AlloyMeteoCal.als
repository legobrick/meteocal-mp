//SIGNATURE
sig RegisteredUser{
calendar:one Calendar
}

sig Invitation{
Receiver: some RegisteredUser,
event:one Event
}

sig Calendar{
}

sig Event{
 Owner:one RegisteredUser
}{
//the number of Invitation must be equals to Event
#Event=#Invitation
}
//FACTS
fact oneCalendar{
//the number of Calendar must be equals to the RegisteredUser
#Calendar=#RegisteredUser
//two or more RegisteredUser cannot refer to the same calendar
no disj r1,r2:RegisteredUser | r1.calendar=r2.calendar
}

fact oneInvitation{
//two or more Invitation cannot refer to the same event 
no disj r,p:Invitation | p.event=r.event //and p.Receiver = r.Receiver
}

fact NotReceveir{
//the owner of an event cannot receive the invitation of its event
all  disj e:Event , i1: Invitation | i1.event=e implies e.Owner not in i1.Receiver 
}
//ASSERTION

assert oneCalendarforUser{
no disj r1,r2:RegisteredUser | r1.calendar=r2.calendar
}
//check oneCalendarforUser
assert  ownerNotReceveir{
all  disj e:Event , i1: Invitation | i1.event=e implies e.Owner not in i1.Receiver 
}
//check ownerNotReceveir
assert oneInvitationforEvent{
no disj r,p:Invitation | p.event=r.event and p.Receiver = r.Receiver
}
//check oneInvitationforEvent
assert cal{
#Calendar=#RegisteredUser
}
//check cal



pred show(){} 
run show 

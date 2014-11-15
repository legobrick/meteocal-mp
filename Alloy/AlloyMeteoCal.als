//SIGNATURE
sig RegisteredUser{
//Participant:some Event
calendar:one Calendar
}
{
//the number of Calendar must be equals to RegisteredUser
#Calendar=#RegisteredUser
}
sig Invitation{
Receiver: some RegisteredUser,
event:one Event
}
sig Calendar{

}
sig Event{
 Owner:one RegisteredUser
}
{
//the number of Invitation must be equals to Event
#Event=#Invitation
}
//FACTS
fact oneCalendar{
//two or more RegisteredUser cannot refer to the same calendar
no disj r1,r2:RegisteredUser | r1.calendar=r2.calendar
//if there is a calendar then there is a registered user
#RegisteredUser-#Calendar=0
 #Calendar>0 implies RegisteredUser.calendar in Calendar
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
assert cal{
#RegisteredUser-#Calendar=0
}
//check cal
//check oneInvitationforEvent

//PREDICATE

pred show(){} 
run show 

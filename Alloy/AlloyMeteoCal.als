sig RegisteredUser{
//Participant:some Event
calendar:one Calendar
}
{
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
#Event=#Invitation
}
fact{
no disj r1,r2:RegisteredUser | r1.calendar=r2.calendar
}
fact{
no disj r,p:Invitation | p.event=r.event and p.Receiver = r.Receiver
//all the owner's event are also participant
 //all disj r:RegisteredUser, e:Event | r=e.Owner implies  r.Participant not in e 
//no   e:Event |  e.Owner= e.Participant
}

fact{
//no i:Invitation,i3:Invitation| i.event=i3.event
//a owner cannot be the receiver
//all disj r:RegisteredUser , i: Invitation | i.Sender=r implies r not in i.Receiver
//the sender is the owner of the event
all  disj e:Event , i1: Invitation | i1.event=e implies e.Owner not in i1.Receiver 
//chi riceve l'invit partecipa all'evento relativ a quell'invito
//all  disj r2:RegisteredUser , i2: Invitation  | i2.Receiver=r2 implies r2.Participant = i2.event
//partecipa solo chi è invitato
//all disj  r3:RegisteredUser , i3: Invitation  | r3 not in i3.Receiver implies r3.Participant not in i3.event 
}

pred show(){} 
run show

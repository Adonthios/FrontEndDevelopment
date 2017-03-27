package nl.hu.frontenddevelopment.Model;

public class ActorPerson {
    public String actorID;
    public boolean canEdit;

    public ActorPerson(){
    }

    public ActorPerson(String actorID, boolean canEdit) {

        this.actorID = actorID;
        this.canEdit = canEdit;
    }

    public String getActorID() {
        return actorID;
    }

    public void setActorID(String actorID) {
        this.actorID = actorID;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    @Override
    public String toString() {
        return "ActorPerson{" +
                "actorID='" + actorID + '\'' +
                ", canEdit=" + canEdit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActorPerson that = (ActorPerson) o;

        if (canEdit != that.canEdit) return false;
        return actorID != null ? actorID.equals(that.actorID) : that.actorID == null;

    }

    @Override
    public int hashCode() {
        int result = actorID != null ? actorID.hashCode() : 0;
        result = 31 * result + (canEdit ? 1 : 0);
        return result;
    }
}

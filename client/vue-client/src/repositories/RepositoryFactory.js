import UsersRepository from "./UsersRepository";
import BooksRepository from "./BooksRepository";
import CodesRepository from "./CodesRepository";
import UnitsRepository from "./UnitsRepository";
import LessonsRepository from "./LessonsRepository";
import ClassGroupsRepository from "./ClassGroupsRepository";
import SubscriptionsRepository from "./SubscriptionsRepository";
import ActivitiesRepository from "./ActivitiesRepository";
import InteractionActivityRepository from "./InteractionActivityRepository";

const repositories = {
  users: UsersRepository,
  books: BooksRepository,
  codes: CodesRepository,
  units: UnitsRepository,
  lessons: LessonsRepository,
  activities: ActivitiesRepository,
  classGroups: ClassGroupsRepository,
  subscriptions: SubscriptionsRepository,
  interactionsActivities: InteractionActivityRepository
};

export default {
  get: name => repositories[name]
};

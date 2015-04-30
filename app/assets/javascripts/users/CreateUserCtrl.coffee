
class CreateUserCtrl

    constructor: (@$log, @$location,  @UserService) ->
        @$log.debug "constructing CreateUserController"
        @user = {}
        @hosts = []

    createUser: () ->
        @$log.debug "createUser()"
        @user.active = true
        @UserService.createUser(@user)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} User"
                @user = data
                @$location.path("/")
            ,
            (error) =>
                @$log.error "Unable to create User: #{error}"
            )

    buildDigitalID: (digitalId) ->
      @$log.debug "buildDigitalID()"
      @UserService.buildDigitalID(digitalId)
      .then(
        (data) =>
            @$log.debug "Promise returned #{data} User"
            @$location.path("/")
          ,
            (error) =>
              @$log.error "Unable to create User: #{error}"
          )


controllersModule.controller('CreateUserCtrl', CreateUserCtrl)
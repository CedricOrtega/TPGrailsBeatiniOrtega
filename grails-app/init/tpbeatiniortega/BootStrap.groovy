package tpbeatiniortega

class BootStrap {

    def init = { servletContext ->
        def userInstance = new User(username: "username",
                password: "password",
                thumbnail: new Illustration(filename: "/assets/image.png")).save(flush: true, failOnError: true)
        (1..5).each{
            userInstance.addToAnnonces(
                    new Annonce(
                        title: "Title",
                        description: "description",
                        validTill: new Date(),
                        state: Boolean.TRUE
            )
                            .addToIllustrations(new Illustration(filename: "filename"))
                            .addToIllustrations(new Illustration(filename: "filename2"))
                            .addToIllustrations(new Illustration(filename: "filename3"))
            )
        }
        userInstance.save(flush:true, failOnError:true)
    }
    def destroy = {
    }
}
